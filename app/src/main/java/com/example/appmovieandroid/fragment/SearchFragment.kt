package com.example.appmovieandroid.fragment

import android.content.Context
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.appmovieandroid.R
import com.example.appmovieandroid.adapters.ListMovieAdapter
import com.example.appmovieandroid.databinding.FragmentSearchBinding
import com.example.appmovieandroid.common.CompanionObject

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    var isDark = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        binding = FragmentSearchBinding.bind(view)


        // load theme state
        isDark = getThemeStatePref()
        handleChangeTheme()

        handleEventListener()


        return view
    }


    private fun handleEventListener() {
        binding.searchInput.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {
                binding.recyclerView.visibility = View.GONE
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
                binding.shimmerFrameLayout.visibility = View.VISIBLE
                binding.shimmerFrameLayout.startShimmerAnimation()
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
//                Log.v("VVV", "onTextChanged")
            }
        })

        binding.searchInput.setOnKeyListener(object : View.OnKeyListener {
            @Suppress("DEPRECATED_IDENTITY_EQUALS")
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (event.action === KeyEvent.ACTION_DOWN &&
                    keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    // Perform action on key press
                    CompanionObject.keyword = binding.searchInput.text.toString()
                    setupAPICall()
                    hideKeyboard()
                    return true
                }
                return false
            }
        })


        binding.fabSwitcher.setOnClickListener {
            isDark = !isDark
            handleChangeTheme()
            saveThemeStatePref(isDark)
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_homeFragment)
        }
    }


    private fun setupAPICall() {
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)


        CompanionObject.getMovieSearchWithKeyWord {
            if (it.isEmpty()) {
                binding.textSearch.text = resources.getString(R.string.no_result_search)
                binding.shimmerFrameLayout.visibility = View.GONE

            } else {
                binding.recyclerView.adapter = ListMovieAdapter(it, requireContext(), false)
                binding.recyclerView.visibility = View.VISIBLE
                binding.textSearch.visibility = View.VISIBLE
                binding.shimmerFrameLayout.visibility = View.GONE
            }
        }
    }


    private fun handleChangeTheme() {
        if (isDark) {
            binding.textSearch.setTextColor(resources.getColor(R.color.card_bg_color))
            binding.searchInput.setBackgroundResource(R.drawable.search_input_dark_style)
            binding.root.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.black
                )
            )
            binding.btnBack.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.white),
                PorterDuff.Mode.MULTIPLY
            )
            @Suppress("DEPRECATION")
            binding.btnFilter.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.white),
                PorterDuff.Mode.MULTIPLY
            )
        } else {
            binding.textSearch.setTextColor(resources.getColor(R.color.black_500))
            binding.searchInput.setBackgroundResource(R.drawable.search_input_style)
            binding.root.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )
            binding.btnBack.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.black),
                PorterDuff.Mode.MULTIPLY
            )
            binding.btnFilter.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.black),
                PorterDuff.Mode.MULTIPLY
            )
        }
    }

    private fun saveThemeStatePref(isDark: Boolean) {
        val pref = requireContext().getSharedPreferences("myPref", AppCompatActivity.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putBoolean("isDark", isDark)
        editor.apply()
    }

    private fun getThemeStatePref(): Boolean {
        val pref =
            requireContext().getSharedPreferences("myPref", AppCompatActivity.MODE_PRIVATE)
        return pref.getBoolean("isDark", false)
    }

    private fun hideKeyboard() {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.searchInput.windowToken, 0)
    }

    override fun onPause() {
        binding.shimmerFrameLayout.stopShimmerAnimation()
        super.onPause()
    }


}