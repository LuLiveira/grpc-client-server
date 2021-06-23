package br.com.lucas.grpc.client

import br.com.lucas.grpc.GrpcComKotlinServiceGrpcKt
import br.com.lucas.grpc.SaveUserRequest
import io.grpc.Channel
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class DemoService {

    suspend fun saveUser() {
        val server = createStub()

        val saveUserRequest = SaveUserRequest.newBuilder()
            .setName("Fernando")
            .setLastName("Queiroz")
            .setDocument("07284650714")
            .build()

        val saveUserResponse = server.saveUser(saveUserRequest)

        println("Usuario registrado com id = " + saveUserResponse.id)
    }

    private fun createStub(): GrpcComKotlinServiceGrpcKt.GrpcComKotlinServiceCoroutineStub {
        val channel: Channel = ManagedChannelBuilder.forAddress("localhost", 50051)
            .usePlaintext()
            .build()

        return GrpcComKotlinServiceGrpcKt.GrpcComKotlinServiceCoroutineStub(channel)
    }

    suspend fun saveUserStream() {
        val demoServerStub = createStub()

        val requests = generateOutgoingRequests()

        demoServerStub.saveUserStream(requests).collect {
            response -> print("Resposta: " + response.id)
        }
    }


    private fun generateOutgoingRequests(): Flow<SaveUserRequest> = flow {

        val request1 = SaveUserRequest.newBuilder()
            .setName("Eduardo")
            .setLastName("Silva")
            .setDocument("05262438594")
            .build()

        val request2 = SaveUserRequest.newBuilder()
            .setName("Carol")
            .setLastName("Souza")
            .setDocument("07262438594")
            .build()

        val request3 = SaveUserRequest.newBuilder()
            .setName("Murilo")
            .setLastName("Oliveira")
            .setDocument("09262438594")
            .build()

        val requests = listOf(request1, request2, request3)

        for (request in requests) {
            println("Requisição: " + request.name)
            emit(request)
            delay(5000)
        }
    }

}