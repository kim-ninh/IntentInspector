package yan.intentinspector

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ExpandableListView
import android.widget.ExpandableListView.OnChildClickListener
import android.widget.SimpleExpandableListAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import yan.intentinspector.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity(), OnChildClickListener {

    private val groupData: MutableList<Map<String, String>> = ArrayList()
    private val listOfChildGroups: MutableList<List<Map<String, String>>> = ArrayList()

    private lateinit var binding: ActivityMainBinding

    private val bottomSheetDialogFragment = TextViewerBottomSheetDialogFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.extras?.let { extras ->
            for (key in extras.keySet()) {
                val value = extras[key]!!

                val grp = HashMap<String, String>(2)
                grp["EXTRA_KEY"] = key
                grp["EXTRA_VALUE_TYPE"] = value::class.java.name
                groupData.add(grp)
                listOfChildGroups.add(populateGroupRow(value))
            }
        }
        val adapter = SimpleExpandableListAdapter(this,
                groupData, android.R.layout.simple_expandable_list_item_2, arrayOf("EXTRA_KEY", "EXTRA_VALUE_TYPE"), intArrayOf(android.R.id.text1, android.R.id.text2),
                listOfChildGroups, R.layout.my_expandable_list_item, arrayOf("EXTRA_VALUE"), intArrayOf(android.R.id.text1)
        )

        with(binding) {
            textViewAction.text = intent.action
            textViewType.text = intent.type
            expandableListView.setOnChildClickListener(this@MainActivity)
            expandableListView.setAdapter(adapter)
        }
    }

    private fun populateGroupRow(v: Any): List<Map<String, String>> {
        val groupRow: MutableList<Map<String, String>> = ArrayList()

        if (v is List<*>) {
            for (subv in v) {
                val sub = HashMap<String, String>(1)
                sub["EXTRA_VALUE"] = subv.toString()
                groupRow.add(sub)
            }
        } else {
            val sub = HashMap<String, String>(1)
            sub["EXTRA_VALUE"] = v.toString()
            groupRow.add(sub)
        }
        return groupRow
    }

    override fun onChildClick(
            parent: ExpandableListView,
            v: View,
            groupPosition: Int,
            childPosition: Int,
            id: Long): Boolean {
        Log.d(TAG, String.format("groupPosition: %d, childPosition: %d, id: %d",
                groupPosition, childPosition, id))
        val childItem = listOfChildGroups[groupPosition][childPosition]

        val extraValue = childItem["EXTRA_VALUE"]!!
        val uri = if (extraValue.startsWith("content://")) {
            extraValue.toUri()
        } else {
            Uri.EMPTY
        }

        val mimeType = contentResolver.getType(uri)

        Log.d(TAG, "Extra value uri: $uri")
        Log.d(TAG, "Extra uri mime type: $mimeType")

        if (mimeType == "text/plain") {
            bottomSheetDialogFragment.arguments = Bundle().apply {
                putParcelable("textUri", uri)
            }

            bottomSheetDialogFragment.show(supportFragmentManager, null)
        }

        return true
    }

    companion object {
        val TAG: String = MainActivity::class.java.simpleName
    }
}