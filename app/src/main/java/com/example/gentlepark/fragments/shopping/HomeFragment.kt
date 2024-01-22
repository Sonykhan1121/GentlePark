package com.example.gentlepark.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.gentlepark.R
import com.example.gentlepark.adapters.HomeViewpagerAdapter
import com.example.gentlepark.databinding.FragmentHomeBinding
import com.example.gentlepark.fragments.categories.KidsFragment
import com.example.gentlepark.fragments.categories.MainCategoryFragment
import com.example.gentlepark.fragments.categories.MenFragment
import com.example.gentlepark.fragments.categories.ShoesFragment
import com.example.gentlepark.fragments.categories.SummerFragment
import com.example.gentlepark.fragments.categories.WinterFragment
import com.example.gentlepark.fragments.categories.WomenFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment:Fragment(R.layout.fragment_home) {
    private lateinit var binding:FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val categoriesFragments = arrayListOf<Fragment>(
            MainCategoryFragment(),
            MenFragment(),
            WomenFragment(),
            KidsFragment(),
            ShoesFragment(),
            WinterFragment(),
            SummerFragment()
        )
        binding.viewpagerhome.isUserInputEnabled = false
        val viewPager2Adapter = HomeViewpagerAdapter(categoriesFragments,childFragmentManager,lifecycle)
        binding.viewpagerhome.adapter = viewPager2Adapter
        TabLayoutMediator(binding.tablayout,binding.viewpagerhome){tab,position ->
            when(position)
            {
                0->tab.text = "Main"
                1->tab.text = "Men"
                2->tab.text = "Women"
                3->tab.text = "Kids"
                4->tab.text = "Shoes"
                5->tab.text = "Winter"
                6->tab.text = "Summer"

            }
        }.attach()

    }

}