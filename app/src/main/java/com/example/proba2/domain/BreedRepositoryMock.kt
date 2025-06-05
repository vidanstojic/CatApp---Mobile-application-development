package com.example.proba2.domain

import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

//class BreedRepositoryMock @Inject constructor() : BreedRepository{
//    override suspend fun getAllBreeds(): List<Breed> {
//        delay(5.seconds)
//        return listOf(
//            Breed(name = "pitina", alternativelyName = "Pitbull", description = "Complicated!"),
//            Breed(name = "staford", alternativelyName = "Straford", description = "Complicated!"),
//            Breed(name = "rotvajler", alternativelyName = "roti", description = "Complicated!"),
//        )
//    }
//}