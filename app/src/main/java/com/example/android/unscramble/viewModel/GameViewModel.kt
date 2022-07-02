package com.example.android.unscramble.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.unscramble.ui.game.MAX_NO_OF_WORDS
import com.example.android.unscramble.ui.game.SCORE_INCREASE
import com.example.android.unscramble.ui.game.allWordsList
import java.lang.Math.random

class GameViewModel : ViewModel() {

    private val _score = MutableLiveData(0)
    private val _currentWordCount = MutableLiveData(0)
    private val _currentScrambledWord = MutableLiveData<String>()
    private lateinit var currentWord: String
    private var wordList: MutableList<String> = mutableListOf()

    //Variables for read in the GameFragment
    val score: LiveData<Int>
        get() = _score

    val currentWordCount: LiveData<Int>
        get() = _currentWordCount

    val currentScrambledWord: LiveData<String>
        get() = _currentScrambledWord


    //Test state the viewModel, when he will be create and destroy
    init {
        Log.d("GameFragment", "GameViewModel created!")
        getNextWord()
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("GameFragment", "GameViewModel destroyed!")
    }

    fun isUserWordCorrect(playerWord: String): Boolean {
        return if (playerWord.equals(currentWord, true)) {
            increaseScore()
            true
        } else {
            false
        }

    }

    /**
     * Function for test if the currentWord exist in the list of word, and scramble the word with shuffle()
     */
    private fun getNextWord() {

        currentWord = allWordsList.random()

        val tempWord = currentWord.toCharArray()

        while (String(tempWord).equals(currentWord, false)) {
            tempWord.shuffle()
        }

        //Test if the currentWord exist in the wordList, because the app don't repeat words used in other round in game
        if (wordList.contains(currentWord)) {
            getNextWord()
        } else {
            _currentScrambledWord.value = String(tempWord)
            _currentWordCount.value = (_currentWordCount.value)?.inc()
            wordList.add(currentWord)
        }


    }

    fun nextWord(): Boolean {
        return if (currentWordCount.value!! < MAX_NO_OF_WORDS) {
            getNextWord()
            true
        } else false

    }

    private fun increaseScore() {
        _score.value = (score.value)?.plus(SCORE_INCREASE)
    }

    fun reinitializeData(){
        _score.value = 0
        _currentWordCount.value = 0
        wordList.clear()
        getNextWord()
    }

}