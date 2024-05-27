package com.example.baddit.data.repository

import android.util.Log
import androidx.annotation.Nullable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.baddit.data.dto.ErrorResponse
import com.example.baddit.data.dto.auth.ChangePasswordRequestBody
import com.example.baddit.data.dto.auth.EmailVerificationRequestBody
import com.example.baddit.data.dto.auth.LoginRequestBody
import com.example.baddit.data.dto.auth.RegisterRequestBody
import com.example.baddit.data.utils.httpToError
import com.example.baddit.data.remote.BadditAPI
import com.example.baddit.data.utils.safeApiCall
import com.example.baddit.domain.error.DataError
import com.example.baddit.domain.error.Result
import com.example.baddit.domain.model.auth.GetMeResponseDTO
import com.example.baddit.domain.model.auth.GetOtherResponseDTO
import com.example.baddit.domain.model.auth.LoginResponseDTO
import com.example.baddit.domain.repository.AuthRepository
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val badditAPI: BadditAPI
) : AuthRepository {

    override val isLoggedIn: MutableState<Boolean> = mutableStateOf(false)

    override val currentUser: MutableState<GetMeResponseDTO?> = mutableStateOf(null)

    init {
        CoroutineScope(Dispatchers.IO).launch {
            getMe()
        }
    }

    override suspend fun login(
        username: String,
        password: String
    ): Result<LoginResponseDTO, DataError.NetworkError> {
        return safeApiCall { badditAPI.login(LoginRequestBody(username, password)) }
    }

    override suspend fun register(
        email: String,
        username: String,
        password: String
    ): Result<Unit, DataError.RegisterError> {
        return safeApiCall(
            apiCall = { badditAPI.signup(RegisterRequestBody(email, username, password)) },
            errorHandler = { response ->
                val errorCode = response.code()
                val errorBody = response.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                if (errorCode == 409) // Means there's a username/email conflict
                    when (errorResponse.error) {
                        "USERNAME_TAKEN" -> Result.Error(DataError.RegisterError.USERNAME_TAKEN)
                        "EMAIL_TAKEN" -> Result.Error(DataError.RegisterError.EMAIL_TAKEN)
                        else -> Result.Error(DataError.RegisterError.UNKNOWN_ERROR)
                    }
                else when (errorCode) {
                    500 -> Result.Error(DataError.RegisterError.INTERNAL_SERVER_ERROR)
                    else -> Result.Error(DataError.RegisterError.UNKNOWN_ERROR)
                }
            }
        )
    }

    override suspend fun getMe(): Result<GetMeResponseDTO, DataError.NetworkError> {
        val result = safeApiCall<GetMeResponseDTO, DataError.NetworkError> { badditAPI.getMe() }

        if (result is Result.Success) {
            isLoggedIn.value = true;
            currentUser.value = result.data;
        }

        Log.d("GetMe", "getMe: ${currentUser.value?.username ?: "null"}")
        return result;
    }

    override suspend fun verifyEmail(token: String): Result<Unit, DataError.NetworkError> {
        return safeApiCall { badditAPI.verify(EmailVerificationRequestBody(token)) }
    }

    override suspend fun getOther(username: String): Result<GetOtherResponseDTO, DataError.NetworkError> {
        return safeApiCall { badditAPI.getOther(username) }
    }

    override suspend fun logout(): Result<Unit, DataError.NetworkError> {
        val result = safeApiCall<Unit, DataError.NetworkError> { badditAPI.logout() }

        if (result is Result.Success) {
            CoroutineScope(Dispatchers.IO).launch {
                isLoggedIn.value = false;
                currentUser.value = null;
                getMe()
            }
        }

        return result
    }

    override suspend fun changePassword(
        oldPassword: String,
        newPassword: String
    ): Result<Unit, DataError.NetworkError> {

        val body = ChangePasswordRequestBody(oldPassword, newPassword)

        return safeApiCall { badditAPI.changePassword(body)}
    }

}