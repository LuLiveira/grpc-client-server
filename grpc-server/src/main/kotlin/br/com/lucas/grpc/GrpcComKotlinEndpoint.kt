package br.com.lucas.grpc

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Singleton

@Singleton
class GrpcComKotlinEndpoint : GrpcComKotlinServiceGrpcKt.GrpcComKotlinServiceCoroutineImplBase() {

    override suspend fun saveUser(request: SaveUserRequest): UserResponse {
        println("Requisicao recebida....");
        println("Requisicao finalizada...");
        return UserResponse.newBuilder()
            .setId(1)
            .setName(request.name)
            .setLastName(request.lastName)
            .build();
    }

    override fun saveUserStream(requests: Flow<SaveUserRequest>): Flow<UserResponse> = flow {
        var id = Math.random().toInt();

        requests.collect {
            println("Salvando usuario...")
            emit (UserResponse.newBuilder().setId(id++).setName(it.name).setLastName(it.lastName).build())
            println ("Concluido...")
        }

    }
}