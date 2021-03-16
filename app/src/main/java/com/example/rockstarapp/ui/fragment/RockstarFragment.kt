package com.example.rockstarapp.ui.fragment

import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.SearchView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.rockstarapp.R
import com.example.rockstarapp.adapter.RockstarListViewAdapter
import com.example.rockstarapp.database.AppDatabase
import com.example.rockstarapp.model.Rockstar
import com.example.rockstarapp.service.RockstarService

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RockstarFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RockstarFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    //Vue
    private lateinit var listViewRockstar: ListView
    private lateinit var adapter: RockstarListViewAdapter
    private lateinit var searchRockstar: SearchView
    private lateinit var refreshRockstar:SwipeRefreshLayout

    private var listRockstar = ArrayList<Rockstar>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_rockstar, container, false)
        listViewRockstar = root.findViewById(R.id.list_rockstars)
        searchRockstar = root.findViewById(R.id.search_bar)
        refreshRockstar = root.findViewById(R.id.refresh_rockstars)

        val db = AppDatabase(requireContext())

        updateListRockstar(db)
        setListener(db)

        return root
    }

    /*
       * Initializes the listener for refreshing the list of rockstars
       * Initializes the listener for the rockstar search bar
       *
       * @param db Instance of room database
       *
       * @return void
        */
    private fun setListener(db:AppDatabase) {

        refreshRockstar.setOnRefreshListener{
            val rockstarService = RockstarService()
            var str_result = rockstarService.execute().get()
            refreshRockstar.isRefreshing = false
            var existingRockstar = db.RockstarDao().getAll()
            for(i in 0 until str_result.size){
                if (!existingRockstar.contains(str_result[i])) {
                    try {
                        db.RockstarDao().insert(str_result[i])
                    }catch (e: SQLiteConstraintException) {
                        e.printStackTrace()
                    }
                }
            }

            updateListRockstar(db)
        }

        searchRockstar.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchRockstar.clearFocus()
                return false
            }

            override fun onQueryTextChange(rockstarSearchText: String?): Boolean {
                adapter.filter.filter(rockstarSearchText)
                return false
            }

        })

    }

    /*
       * Update the rockstars listViewRockstar from the room db
       *
       * @param db Instance of room database
       *
       * @return void
        */
    private fun updateListRockstar(db:AppDatabase) {

        listRockstar.clear()
        var rockList = db.RockstarDao().getAll()
        if (rockList.isNotEmpty()){
            for(rockstar in rockList) {
                listRockstar.add(rockstar)
            }
        }

        adapter = RockstarListViewAdapter(requireActivity(),listRockstar,false)
        listViewRockstar.adapter = adapter
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RockstarFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RockstarFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}