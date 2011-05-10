/* © 2010 Stephan Reichholf <stephan at reichholf dot net>
 * 
 * Licensed under the Create-Commons Attribution-Noncommercial-Share Alike 3.0 Unported
 * http://creativecommons.org/licenses/by-nc-sa/3.0/
 */

package net.reichholf.dreamdroid.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import net.reichholf.dreamdroid.R;
import net.reichholf.dreamdroid.abstivities.AbstractHttpActivity;
import net.reichholf.dreamdroid.abstivities.AbstractHttpFragmentActivity;

/**
 * @author sre
 *
 */
public class MainFragmentActivity extends AbstractHttpFragmentActivity {

	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_fragmented);
		
	}
	
	public static class DetailActivity extends AbstractHttpActivity{
		
	}
	
	
	public static class DetailFragment extends Fragment{
		
	}
	
	public static class MenuFragment extends Fragment{
		
	}
}
