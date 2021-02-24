package yan.intentinspector

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ExpandableListView
import android.widget.ExpandableListView.OnChildClickListener
import android.widget.SimpleExpandableListAdapter
import androidx.appcompat.app.AppCompatActivity
import yan.intentinspector.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity(), OnChildClickListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val groupData: MutableList<Map<String, String>> = ArrayList()
        val listOfChildGroups: MutableList<List<Map<String, String>>> = ArrayList()

        intent.extras?.let { extras ->
            for (key in extras.keySet()) {
                val value = extras[key]!!

                val grp = HashMap<String, String>()
                grp["EXTRA_KEY"] = key
                grp["EXTRA_VALUE_TYPE"] = value::class.java.name
                groupData.add(grp)
                listOfChildGroups.add(populateGroupRow(value))
            }
        }
        val adapter = SimpleExpandableListAdapter(this,
                groupData, android.R.layout.simple_expandable_list_item_2, arrayOf("EXTRA_KEY", "EXTRA_VALUE_TYPE"), intArrayOf(android.R.id.text1, android.R.id.text2),
                listOfChildGroups, R.layout.my_expandable_list_item, arrayOf("CHILD_NAME"), intArrayOf(android.R.id.text1)
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
            val sub = HashMap<String, String>()
            for (subv in v) {
                sub["CHILD_NAME"] = subv.toString()
                groupRow.add(sub)
            }
        } else {
            val sub = HashMap<String, String>()
            sub["CHILD_NAME"] = v.toString()
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
        return true
    }

    companion object {
        val TAG: String = MainActivity::class.java.simpleName
    }
}