package com.kl3jvi.feature_home.bindings

import android.graphics.Color
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kl3jvi.model.Status

@BindingAdapter("isFavorite")
fun ImageButton.isFavorite(state: Boolean) {
    isActivated = state
}

@BindingAdapter("isFavorite")
fun FloatingActionButton.isFavorite(state: Boolean) {
    isActivated = state
}

@BindingAdapter("statusText")
fun TextView.updateTextByStatus(status: Status) {
    when (status) {
        Status.OPEN -> {
            text = "Open"
            setTextColor(Color.parseColor("#4CAF50"))
        }

        Status.ORDER_AHEAD -> {
            text = "Order Ahead"
            setTextColor(Color.parseColor("#efb700"))
        }

        Status.CLOSED -> {
            text = "Closed"
            setTextColor(Color.parseColor("#b81d13"))
        }
    }
}


@BindingAdapter("statusTextOnly")
fun TextView.changeTextByStatus(status: Status) {
    text = when (status) {
        Status.OPEN -> "Open"
        Status.ORDER_AHEAD -> "Order Ahead"
        Status.CLOSED -> "Closed"
    }
}

@BindingAdapter("cardColorByStatus")
fun CardView.changeCardColorByStatus(status: Status) {
    when (status) {
        Status.OPEN -> setCardBackgroundColor(Color.parseColor("#4CAF50"))
        Status.ORDER_AHEAD -> setCardBackgroundColor(Color.parseColor("#efb700"))
        Status.CLOSED -> setCardBackgroundColor(Color.parseColor("#b81d13"))
    }
}
