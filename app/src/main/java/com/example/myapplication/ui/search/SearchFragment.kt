package com.example.myapplication.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SearchView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var searchResultsView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var searchResultsAdapter: SearchResultsAdapter
    private val viewModel: SearchViewModel by viewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize the search view widget
        searchView = binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle search query submit
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Handle search query text change
                return true
            }
        })

        // Initialize drawer and results
        searchResultsView = binding.searchResults
        // Set up RecyclerView
        searchResultsView.layoutManager = GridLayoutManager(requireContext(), 2)
        searchResultsAdapter = SearchResultsAdapter(emptyList()) { service ->
            // Handle service click
        }
        searchResultsView.adapter = searchResultsAdapter

        // Set up ViewModel and observe search results
        viewModel.searchResults.observe(viewLifecycleOwner) { results ->
            searchResultsAdapter.items = results
            searchResultsAdapter.notifyDataSetChanged()
        }

        // Set up DrawerLayout
        drawerLayout = binding.drawerLayout
        val toggle = ActionBarDrawerToggle(
            requireActivity(),
            drawerLayout,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Set up RecyclerView
        val servicesRecyclerView = binding.servicesRecyclerView
        val layoutManager = GridLayoutManager(requireContext(), 2)
        servicesRecyclerView.layoutManager = layoutManager

        // Set up adapter
        val services = listOf("Service 1", "Service 2", "Service 3") // Replace with your services
        val servicesAdapter = ServicesAdapter(services) { service ->
            // Handle item click
        }
        servicesRecyclerView.adapter = servicesAdapter

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
