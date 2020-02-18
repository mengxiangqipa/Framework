/*
 *  Copyright (c) 2020 YobertJomi
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.framework.customview.scardview

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.support.annotation.ColorInt


internal interface SCardViewImpl {
    fun initialize(cardView: SCardViewDelegate, context: Context, backgroundColor: ColorStateList,
                   radius: Float, elevation: Float, maxElevation: Float, direction: Int, cornerVisibility: Int, startColor: Int = -1, endColor: Int = -1)

    fun setRadius(cardView: SCardViewDelegate, radius: Float)

    fun getRadius(cardView: SCardViewDelegate): Float

    fun setElevation(cardView: SCardViewDelegate, elevation: Float)

    fun getElevation(cardView: SCardViewDelegate): Float

    fun initStatic()

    fun setMaxElevation(cardView: SCardViewDelegate, maxElevation: Float)

    fun getMaxElevation(cardView: SCardViewDelegate): Float

    fun getMinWidth(cardView: SCardViewDelegate): Float

    fun getMinHeight(cardView: SCardViewDelegate): Float

    fun updatePadding(cardView: SCardViewDelegate)

    fun onCompatPaddingChanged(cardView: SCardViewDelegate)

    fun onPreventCornerOverlapChanged(cardView: SCardViewDelegate)

    fun setBackgroundColor(cardView: SCardViewDelegate, color: ColorStateList?)

    fun setShadowColor(cardView: SCardViewDelegate, @ColorInt startColor: Int, @ColorInt endColor: Int)

    fun getBackgroundColor(cardView: SCardViewDelegate): ColorStateList

    fun getShadowBackground(cardView: SCardViewDelegate): Drawable

    fun setColors(cardView: SCardViewDelegate, backgroundColor: Int, shadowStartColor: Int, shadowEndColor: Int)
}
