package yan.intentinspector;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView infoAction = (TextView) findViewById(R.id.textViewAction);
        TextView infoType = (TextView) findViewById(R.id.textViewType);
        Intent receivedIntent = getIntent();
        String receivedInfo = receivedIntent.getAction();
        if(receivedInfo != null) {
            infoAction.setText(receivedInfo);
        }

        receivedInfo = receivedIntent.getType();
        if(receivedInfo != null) {
            infoType.setText(receivedInfo);
        }

        List<Map<String, String>> groupData = new ArrayList<>();
        List<List<Map<String, String>>> listOfChildGroups = new ArrayList<>();

        Bundle bundle = receivedIntent.getExtras();
        if(bundle != null) {
            for (String key : bundle.keySet()) {
                Object v = bundle.get(key);
                HashMap<String, String> grp = new HashMap<>();
                grp.put("ROOT_NAME", String.format("%s (%s)", key , getObjectType(v)));
                groupData.add(grp);
                listOfChildGroups.add( populateGroupRow(v));
            }
        }

        SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(this,

                groupData, android.R.layout.simple_expandable_list_item_1,
                new String[] { "ROOT_NAME"}, new int[] {android.R.id.text1},

                listOfChildGroups, android.R.layout.simple_expandable_list_item_2,
                new String[] { "CHILD_NAME", "CHILD_NAME"}, new int[] {android.R.id.text1, android.R.id.text2}
            );

        ExpandableListView elv = (ExpandableListView) findViewById(R.id.expandableListView);
        elv.setAdapter(adapter);
    }

    private String getObjectType(Object v) {
        String info = v.getClass().toString();
        if(info.startsWith("class ")) {
            info = info.substring(6);
        }
        return info;
    }

    private List<Map<String, String>> populateGroupRow(Object v){
        List<Map<String, String>> groupRow = new ArrayList<>();
        if(v instanceof List) {
            List<Object> info =  (List<Object>) v;
            for(Object subv : info) {
                HashMap<String, String> sub = new HashMap<>();
                sub.put("CHILD_NAME", subv.toString());
                groupRow.add(sub);
            }
        }
        else {
            HashMap<String, String> sub = new HashMap<>();
            sub.put("CHILD_NAME", v.toString());
            groupRow.add(sub);
        }
        return groupRow;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
