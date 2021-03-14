package com.example.rockstarapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.rockstarapp.R
import com.example.rockstarapp.database.AppDatabase
import com.example.rockstarapp.model.Rockstar
import org.json.JSONException
import org.json.JSONObject

class RockstarListViewAdapter(private val context: Context,
                              private var dataSource: ArrayList<Rockstar>,
                              private var bookmarkView:Boolean) : BaseAdapter(),Filterable {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private lateinit var bookmarks:TextView
    private lateinit var rockstarStatus:TextView
    private lateinit var rockstarFullName:TextView
    private lateinit var rockstarAddBookmark:CheckBox
    private lateinit var rockstarDeleteBookmark:ImageButton
    private lateinit var rockstar:Rockstar

    private  var listRockstar:ArrayList<Rockstar> = dataSource
    var filterdListRockstar:ArrayList<Rockstar> = ArrayList()

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view:View = convertView ?: inflater.inflate(R.layout.item_rockstar, null)
        rockstar = dataSource[position]

        rockstarFullName = view.findViewById(R.id.rockstar_full_name)
        rockstarStatus = view.findViewById(R.id.rockstar_status)
        rockstarAddBookmark = view.findViewById(R.id.add_bookmark)
        rockstarDeleteBookmark = view.findViewById(R.id.delete_bookmark)

        if (dataSource[position] != null) {
            rockstarFullName.text = dataSource[position].lastName + " " + dataSource[position].firstName
            rockstarStatus.text = dataSource[position].status
            rockstarAddBookmark.isChecked = dataSource[position].bookmark
        }

        if (bookmarkView){
            rockstarDeleteBookmark.visibility = View.VISIBLE
            rockstarAddBookmark.visibility = View.INVISIBLE
        }else {
            rockstarAddBookmark.visibility = View.VISIBLE
            rockstarDeleteBookmark.visibility = View.INVISIBLE
        }
        checkboxListener(rockstarAddBookmark,context,dataSource[position])
        buttonListener(rockstarDeleteBookmark,context,dataSource[position])

        return view
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    private fun checkboxListener(bookmark: CheckBox, context: Context, rockstar: Rockstar) {
        bookmark.setOnClickListener {
            val db = AppDatabase(context)
            rockstar.bookmark = bookmark.isChecked
            db.RockstarDao().updateRockstar(rockstar)

        }
    }

    private fun buttonListener(deleteBookmark: ImageButton, context: Context, rockstar: Rockstar) {
        deleteBookmark.setOnClickListener {
            val db = AppDatabase(context)
            rockstar.bookmark = false
            dataSource.remove(rockstar)
            db.RockstarDao().updateRockstar(rockstar)
            notifyDataSetChanged()
        }
    }

    override fun getFilter(): Filter {
        return object: Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch:String = constraint.toString()

                filterdListRockstar = if (charSearch.isEmpty()){
                    listRockstar
                }else{
                    var returnListRockstar:ArrayList<Rockstar> = ArrayList()
                    for (rockstar in listRockstar) {
                        if (rockstar.lastName.toLowerCase().contains(charSearch.toLowerCase()) ||
                            rockstar.firstName.toLowerCase().contains(charSearch.toLowerCase())){
                            returnListRockstar.add(rockstar)
                        }
                    }
                    returnListRockstar
                }
                val filterResult = FilterResults()
                filterResult.count = filterdListRockstar.count()
                filterResult.values = filterdListRockstar
                return filterResult
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                dataSource = results?.values as ArrayList<Rockstar>
                notifyDataSetChanged()
            }

        }
    }

}