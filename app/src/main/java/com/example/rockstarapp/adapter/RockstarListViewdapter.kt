package com.example.rockstarapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.rockstarapp.R
import com.example.rockstarapp.database.AppDatabase
import com.example.rockstarapp.model.Rockstar


class RockstarListViewAdapter(private val context: Context,
                              private var dataSource: ArrayList<Rockstar>,
                              private var bookmarkView:Boolean) : BaseAdapter(),Filterable {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private lateinit var rockstarStatus:TextView
    private lateinit var rockstarFullName:TextView
    private lateinit var rockstarAddBookmark:CheckBox
    private lateinit var rockstarDeleteBookmark:ImageButton
    private lateinit var rockstarImage:ImageView
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
        rockstarImage = view.findViewById(R.id.rockstar_img)

        if (dataSource[position] != null) {
            rockstarFullName.text = dataSource[position].lastName + " " + dataSource[position].firstName
            rockstarStatus.text = dataSource[position].status
            rockstarAddBookmark.isChecked = dataSource[position].bookmark

            var requestOptions = RequestOptions()
            requestOptions = requestOptions.transforms(CenterCrop(), RoundedCorners(5))
            Glide.with(context).load(dataSource[position].picture)
                .centerCrop()
                .apply(requestOptions)
                .into(rockstarImage)
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

    /*
    * Initialize a listener on the add_bookmark checkbox to update the data related to the rockstar
    *
    * @param bookmark The star checkbox in the view,to add the rockstar in boorkmark list
    * @param context  the execution context
    * @param rockstar rockstar object associate to the checkbox
    *
    * @return void
     */
    private fun checkboxListener(bookmark: CheckBox, context: Context, rockstar: Rockstar) {
        bookmark.setOnClickListener {
            val db = AppDatabase(context)
            rockstar.bookmark = bookmark.isChecked
            db.RockstarDao().updateRockstar(rockstar)

        }
    }

    /*
       * Initialize a listener on the delete_bookmark button to remove a rockstar from the Bookmarked list
       *
       * @param bookmark The trash button in the view, to delete the rockstar
       * @param context  The context execution
       * @param rockstar rockstar object associate to the button
       *
       * @return void
        */
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