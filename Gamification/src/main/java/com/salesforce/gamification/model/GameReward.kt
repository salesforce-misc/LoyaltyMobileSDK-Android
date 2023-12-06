package com.salesforce.gamification.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class GameReward(
    @SerializedName("color")
    val segColor: String?,

    @SerializedName("description")
    val description: String?,

    @SerializedName("expirationDate")
    val expirationDate: String?,

    @SerializedName("imageUrl")
    val imageUrl: String?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("rewardDefinitionId")
    val rewardDefinitionId: String?,

    @SerializedName("rewardType")
    val rewardType: String?,

    @SerializedName("rewardValue")
    val rewardValue: String?,

    @SerializedName("gameRewardId")
    val gameRewardId: String?,
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(segColor)
        parcel.writeString(description)
        parcel.writeString(expirationDate)
        parcel.writeString(imageUrl)
        parcel.writeString(name)
        parcel.writeString(rewardDefinitionId)
        parcel.writeString(rewardType)
        parcel.writeString(rewardValue)
        parcel.writeString(gameRewardId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GameReward> {
        override fun createFromParcel(parcel: Parcel): GameReward {
            return GameReward(parcel)
        }

        override fun newArray(size: Int): Array<GameReward?> {
            return arrayOfNulls(size)
        }
    }
}