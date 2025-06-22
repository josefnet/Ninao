package com.costostudio.ninao.presentation.util.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun GenreSelectionComponent(
    selectedGender: Int,
    onGenderSelected: (Int) -> Unit
) {
    val options = listOf(0,1)

    Column {
        Text("Genre", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            options.forEach { gender ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable { onGenderSelected(gender) }
                        .padding(0.dp)
                ) {
                    RadioButton(
                        selected = gender == selectedGender,
                        onClick = { onGenderSelected(gender) }
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = if (gender == 0) "Homme" else "Femme")
                }
            }
        }
    }
}

@Preview(name = "GenreSelectionComponentHomme", showBackground = true)
@Composable
fun GenreSelectionComponentMalePreview() {
    GenreSelectionComponent(
        selectedGender = 0,
        onGenderSelected = {}
    )
}

@Preview(name = "GenreSelectionComponentFemme", showBackground = true)
@Composable
fun GenreSelectionComponentFemalePreview() {
    GenreSelectionComponent(
        selectedGender = 1,
        onGenderSelected = {}
    )
}