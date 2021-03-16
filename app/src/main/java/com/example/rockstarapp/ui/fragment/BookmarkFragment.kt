package com.example.rockstarapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.example.rockstarapp.R
import com.example.rockstarapp.database.AppDatabase
import com.example.rockstarapp.model.Rockstar
import com.example.rockstarapp.adapter.RockstarListViewAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BookmarkFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BookmarkFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    //Vue
    private lateinit var listViewRockstarBookmarked: ListView
    private lateinit var listRockstarBookmared:ArrayList<Rockstar>
    private lateinit var adapter: RockstarListViewAdapter

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
        val root = inflater.inflate(R.layout.fragment_bookmark, container, false)
        listViewRockstarBookmarked = root.findViewById(R.id.list_rockstars_bookmarked)

        setBookmarkedListRockstar()

        adapter = RockstarListViewAdapter(requireActivity(),listRockstarBookmared,true)
        listViewRockstarBookmarked.adapter = adapter
        return root
    }

    /*
       * Init list of bookmarked rockstars
       *
       * @param db Instance of the room db
       *
       * @return void
    */
    private fun setBookmarkedListRockstar() {
        listRockstarBookmared = ArrayList()
        val db = AppDatabase(requireContext())
        var bookmarkedRockstar = db.RockstarDao().findByBookmark(true)

        if (bookmarkedRockstar.isNotEmpty()){
            for(rockstar in bookmarkedRockstar) {
                listRockstarBookmared.add(rockstar)
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BookmarkFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BookmarkFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}