/*
 * Copyright (c) 2023, Salesforce, Inc.
 * All rights reserved.
 * SPDX-License-Identifier: BSD-3-Clause
 * For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */

package com.salesforce.gamification.api

import com.salesforce.gamification.model.GameRewardResponse
import com.salesforce.gamification.model.Games
import retrofit2.http.*

/**
 * GameAPIInterface class holds the RESTAPI call definitions for all Gamification APIs.
 */
interface GameAPIInterface {

    @GET()
    suspend fun getGameReward(
        @Url url: String,
    ): Result<GameRewardResponse>

    @GET()
    suspend fun getGames(
        @Url url: String,
        @Query("gameParticipantRewardId") gameParticipantRewardId: String?
    ): Result<Games>
}