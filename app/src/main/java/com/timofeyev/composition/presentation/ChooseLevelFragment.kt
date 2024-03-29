package com.timofeyev.composition.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.timofeyev.composition.R
import com.timofeyev.composition.databinding.FragmentChooseLevelBinding
import com.timofeyev.composition.domain.entity.Level

class ChooseLevelFragment: Fragment() {

  private var _binding: FragmentChooseLevelBinding? = null
  private val binding: FragmentChooseLevelBinding
  get() = _binding ?: throw RuntimeException("ChooseLevelFragment")

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentChooseLevelBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setButtonClickListeners()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  private fun launchGameFragment(level: Level) {
    requireActivity().supportFragmentManager.beginTransaction()
      .replace(R.id.main_container, GameFragment.newInstance(level))
      .addToBackStack(GameFragment.NAME)
      .commit()
  }

  private fun setButtonClickListeners() {
    with(binding) {
      buttonLevelTest.setOnClickListener {
        launchGameFragment(Level.TEST)
      }
      buttonLevelEasy.setOnClickListener {
        launchGameFragment(Level.EASY)
      }
      buttonLevelNormal.setOnClickListener {
        launchGameFragment(Level.NORMAL)
      }
      buttonLevelHard.setOnClickListener {
        launchGameFragment(Level.HARD)
      }
    }
  }

  companion object {
    const val NAME = "ChooseLevelFragment"

    fun newInstance(): ChooseLevelFragment {
      return ChooseLevelFragment()
    }
  }
}