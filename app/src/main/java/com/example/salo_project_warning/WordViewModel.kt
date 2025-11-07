package com.example.salo_project_warning

import android.content.Context
import android.net.Uri
import android.widget.TextView
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.salo_project_warning.models.WordItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.collections.emptySet

// определяем экземпляр в локальноой памяти DataStore

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "saved_words")


inline fun <reified T> Gson.fromJson(json: String): T? {
    return try {
        fromJson(json, T::class.java)
    } catch (e: Exception) {
        null
    }
}

class WordViewModel : ViewModel() {

    // Cхраняет текст находящийся в текстовом поле ввода

    // в локальной памяти в данный момент (в текущем State состоянии интерфейса))

    var textFieldState = mutableStateOf("")

    private val gson = Gson()

    //Придумываем свой ключи и поток хранилиша данных DataStore

    private val wordsKey = stringPreferencesKey("model_json_list")

    //поток который ведет список сохраненывз слов при каждом его изменении



    fun getWords(context: Context): Flow<List<WordItem>>{
        return context.dataStore.data.map { preferences ->
            val jsonString = preferences[wordsKey] ?: "[]" //дает ввиде Json-строки из DataStore
            //Создаем TypeToken, который описывает точный тип: список WordItems.
            val type = object : TypeToken<List<WordItem>>() {}.type
            //Передаем этот 'type' в fromJson. Теперь Gson знает, какой тип модели создавать.
            // Безопасное приведение типов 'as?' и оператор элвис '?:' обрабатывают те случаи, когда

            // сохраненные данные (картинка, текст) могут быть повреждены или пусты.
            (gson.fromJson<Any?>(jsonString,type)as? List<WordItem>)?:emptyList()

        }


    }

    // логика введения в локальной DataStore ViewModel

    // слова из входящкго аргумента функциии OnTextFieldChange

    // В любом месте вызова этой функцции

    fun onTextFieldChange(newValue: String) {

        textFieldState.value = newValue
    }

    fun saveModel(context: Context, text: String, imageUri: String) {

        // не сохроняем если текстовое поле пустое, или
        //Поручаем сохранение текста - обьекту ViewModel - если текст есть:

        if (textFieldState.value.isBlank())
            return

        viewModelScope.launch {
            if (text.isBlank()) {
                Toast.makeText(context, "Model cannot be empty", Toast.LENGTH_LONG).show()
                return@launch
            }

            context.dataStore.edit {preferences ->
                //получить текущий набор слов или создать новый пустой набор
                // в окне textFieldState

                val currentListJson = preferences[wordsKey] ?: "[]"

                val token = object : TypeToken<MutableList<WordItem>>() {}.type

                val currentList = (gson.fromJson<Any?>(currentListJson,token)
                        as? MutableList<WordItem>) ?: mutableListOf()

                currentList.add(WordItem(text = text, imageUri = imageUri))
                val jsonToWrite: String = gson.toJson(currentList)
                preferences[wordsKey] = jsonToWrite

            }

            // чищаем в локальной памяти текстовое поле
            // textFieldState после сохранения текущего слова
            textFieldState.value = ""
            Toast.makeText(context, "Model Saved!", Toast.LENGTH_LONG).show()



        }




    }
}