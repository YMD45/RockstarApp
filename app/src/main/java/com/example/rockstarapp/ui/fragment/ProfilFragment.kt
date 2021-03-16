package com.example.rockstarapp.ui.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.example.rockstarapp.R
import com.example.rockstarapp.database.AppDatabase
import com.example.rockstarapp.model.Profil
import java.io.File

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfilFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfilFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    //Vue
    private lateinit var saveButton:ImageButton
    private lateinit var imageProfil:ImageView
    private lateinit var editNameProfil:EditText

    private  var profil: Profil = Profil( "" ,"")
    private var profilExist:Boolean =false

    //For profile image management
    private val imgId = 70
    private var imgUri: Uri? = null
    private val AUTHORITY = "com.example.rockstarapp.fileprovider"



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
        val root = inflater.inflate(R.layout.fragment_profil, container, false)
        val db = AppDatabase(requireContext())
        profilExist = checkProfilExist(db)
        if (profilExist) {
            profil = db.ProfilDao().getProfil()
        }

        saveButton = root.findViewById(R.id.save_profil)
        imageProfil = root.findViewById(R.id.imageProfile)
        editNameProfil = root.findViewById(R.id.edit_name_profil)

        editNameProfil.setText(profil.fullName)
        imageProfil.setImageURI(getImgUri())
        generateListener(profilExist,db)

        return root
    }

    /*
       * Get the profil image Uri to show it in imageProfile
       *
       *
       * @return Uri the Uri picture
    */
    private fun getImgUri():Uri?{
        if (imgUri!=null){
            return imgUri
        }else{
            return Uri.parse(profil.image)
        }
    }

    private fun checkProfilExist(db: AppDatabase):Boolean = db.ProfilDao().getProfil() != null


    private fun generateListener(profilExist:Boolean,db:AppDatabase){
        saveButton.setOnClickListener {

            profil.fullName = getFullName(editNameProfil)
            if(imgUri != null){
                profil.image = imgUri.toString()
                cleanImgFolder(profil.image)
            }

            //If profil doesn't exist, we create it
            if (profilExist)
                db.ProfilDao().updateProfil(profil)
            else
                db.ProfilDao().insert(profil)

            Toast.makeText(requireContext(),"Votre profil est sauvegardé !",Toast.LENGTH_SHORT).show()
        }

        imageProfil.setOnClickListener(){

            var imgFolder = File(requireContext().filesDir,"images");
            if (!imgFolder.exists())
                imgFolder.mkdir()

            var image = File(imgFolder,
                File.separator+System.currentTimeMillis() + "profil.png");

            imgUri = FileProvider.getUriForFile(
                requireContext(),
                AUTHORITY,
                image);

            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent,imgId)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == imgId && resultCode == RESULT_OK) {
            profil.image = imgUri.toString()
            imageProfil.setImageURI(imgUri)
            saveButton.isClickable = true
        }
    }

    /*
       * We keep all picture taken until one is saved,
       * to avoid having black screen when we change fragment
       *
       * This method is used to delete old images.
       *
       * @param imageProfilUri The imageUri choosen to be saved
       *
       * @return void
    */
    private fun cleanImgFolder(imageProfilUri : String) {
        var imgFolder = File(requireContext().filesDir,"images");
        val segments: List<String> = imageProfilUri.split("/")
        var imgProfilName = segments[segments.size-1]
        val children: Array<String> = imgFolder.list()
        for (i in children.indices) {
            if(children[i] != imgProfilName){
                File(imgFolder, children[i]).delete()
            }
        }
    }

    /*
       * Get the name written in the edit_name_profil
       *
       *
       * @param editTextProfil The EditText containing the full name
       *
       * @return The value of EditText or empty string if it's empty
    */
    private fun getFullName(editTextProfil:EditText):String = if (editTextProfil.text!=null){
        editTextProfil.text.toString()
    } else {
        ""
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfilFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfilFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}