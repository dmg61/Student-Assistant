package com.example.studentassistant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

	// �������� �������� (�����)
	public static String[] groups = new String[] { "��������",
			"������������ ������", "��������� � �����", "������",
			"������������� � ���������", "������� ������", "������� ������" };

	// �������� ��������� (���������)
	public static String[] mehanika = new String[] { "x=x0+v0t+(at^(2))/2",
			"v=v0+at", "a=(v-v0)/t", "a=(v^2-v0^2)/2S", "S=v0t+(at^2)/2",
			"w=v/R - ������� ��������", "w=2�/T", "v=2�R/T", "a=(v^2)/R",
			"F=ma", "F=G(m1m2)/R^2", "F=-kx", "L=Iw", "M=Fl - ������ ����",
			"p=mv - �������", "A=FS - ������", "W=A/t - ��������",
			"E�=E���.+E���. - ������ �������", "E���.=(mv^2)/2", "E���.=mgh",
			"E���.=(kx^2)/2 - ��� �������" };
	public static String[] molekula = new String[] { "M=m/v - �������� �����",
			"m0=M/Na (Na - ����� ��������)", "p=nkT", "E=3/2kT", "n=N/V",
			"pV=vRT - ��������� ����������-����������",
			"R=kNa (Na - ����� ��������)",
			"U=Q-A - ������ ������ �������������",
			"Q=cm(t2-t1) - ������� ����������� �� ������(����������)",
			"Q=�m - ��������������(���������),(� - ������)", "Q=qm - ��������",
			"Q=rm - ���������������(�����������)" };
	public static String[] volni = new String[] {
			"x=Acos(wt+�) - ��������� ������������� ���������",
			"T=2�/w - ������", "a=vT - ����� �����(� - ������)",
			"T=2�sqrt(l/g) - ���.�������", "T=2�sqrt(LC) - ���.������",
			"T=2�sqrt(m/g) - ����.�������", "w=sqrt(g/l) - ���.�������",
			"w=sqrt(g/m) - ����.�������", "w=1/sqrt(LC) - ���.������" };
	public static String[] optika = new String[] {
			"1/f+1/d=1/F=� - ������� �����",
			"sin a / sin b = n2/n1  - ����� ����������� (a,b - ���� �������,���������)",
			"K=f/d - ���������� �����" };
	public static String[] elektrichestvo = new String[] {
			"F� = |q1||q2|/(4��r^2) - ����������� ����",
			"E=F/q - ������������� ��.����", "�=ES - ����� ����� �������� S",
			"�=W/q - ���������", "A=q(�1-�2)", "U=�1-�2",
			"C=q/U - ������� �����������", "U=A/q",
			"W=(q^2)/(2C) - ������� �������� ������������", "I=q/t", "I=U/R",
			"I=E/(R+r)", "R=�l/s", "Q=A=IUt - ����� ������-�����",
			"F�=Bqv - ���� �������", "Fa=BIl - ���� ������", "L=�/I",
			"W=LI^2/2 - ������� ���������� ����" };
	public static String[] atom = new String[] {
			"m=Z*M�+N*M�-M� - ������ ����", "E=hv - ������� ������",
			"p=hv/c - ������� ������",
			"a = h/mc - ����� ��-������(� - ������)",
			"M=mvr - ����������� ������������ ������ ���������",
			"P�=IS=ev�r^2 - ����������� ��������� ������" };
	public static String[] yadro = new String[] {
			"Pm/M=-e/2m - ������������� ���������",
			"T=ln(2)/a = 0.6931/a - ������ ����������� (� - ������)" };
	// ��������� ��� �����
	public static ArrayList<Map<String, String>> groupData;

	// ��������� ��� ��������� ����� ������
	public static ArrayList<Map<String, String>> childDataItem;

	// ����� ��������� ��� ��������� ���������
	public static ArrayList<ArrayList<Map<String, String>>> childData;
	// � ����� ��������� childData = ArrayList<childDataItem>

	// ������ ���������� ������ ��� ��������
	public static Map<String, String> m;

	public static ExpandableListView elvMain;

	// ���������� ����������
	private static Button bCalculate; // ������ "���������"
	private static Button bHelp; // ������ "������"
	private static EditText enterFormula; // ������� ����� �������
	private static EditText enterA; // ������ ������� ��������������
	private static EditText enterB; // ������� ������� ��������������
	private static EditText enterEr; // ������ ����������
	private static TextView answer; // ���� ������ ������
	private static TextView answerLable; // ���� ������
	
	private static long firstPress, secondPress;

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mViewPager.setCurrentItem(1);
		
		firstPress  = 0;
		secondPress = 0;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public void onBackPressed()
	{
		secondPress = System.currentTimeMillis(); 
		
		if (firstPress != 0 && (secondPress - firstPress < 1000))
		{
			MainActivity.super.onBackPressed();
		}
		else
		{
			//mViewPager.setCurrentItem(1);
			Toast.makeText(getApplication(), "������� ��� ��� ��� ������", Toast.LENGTH_SHORT).show();
			firstPress = secondPress;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.inMain:
			mViewPager.setCurrentItem(1);
			return true;
		case R.id.autor:

			AlertDialog.Builder builder = new AlertDialog.Builder(
					MainActivity.this);
			builder.setTitle("� ���������")
					.setMessage(
							"��������� ������������� ��� ��������.\n�������� ������� ���������� ������������� ���������, ������ ���� ���������� ������.\n�������� ���������� �� ���������� ����������(��. \"���������� ����������\" ������ \"�������\" ��� ��. ����).\n\n������:\n�������� �. �.\n������� �. �.\n���������� �. �.\n����������� �. �.\n��������� �. �.")
					.setIcon(R.drawable.ic_launcher)
					.setCancelable(false)
					.setNegativeButton("������� �� ����������",
							new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// ����������� �������
							Intent help = new Intent(getApplication(), IntegralHelp.class);
							startActivity(help);
						}
					})
					.setPositiveButton("�������",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// �������� ����������� ����
									dialog.cancel();
								}
							});

			AlertDialog dialog = builder.create();
			dialog.show();

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.integralMenu).toUpperCase(l);
			case 1:
				return getString(R.string.menu).toUpperCase(l);
			case 2:
				return getString(R.string.formulMenu).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			int currentNumebr = getArguments().getInt(ARG_SECTION_NUMBER) - 1;
			View rootView;

			if (currentNumebr == 0) // ���������� ����������
			{
				rootView = inflater.inflate(R.layout.integral_main, container,
						false);

				// ������������ ������ � �����
				bCalculate = 	(Button) rootView.findViewById(R.id.calculation);
				bHelp = 	 	(Button) rootView.findViewById(R.id.integral_help);
				enterFormula = 	(EditText) rootView.findViewById(R.id.formula);
				enterA = 		(EditText) rootView.findViewById(R.id.bottomBorder);
				enterB = 		(EditText) rootView.findViewById(R.id.topBorder);
				enterEr = 		(EditText) rootView.findViewById(R.id.accuracy);
				answer = 		(TextView) rootView.findViewById(R.id.integral_answer);
				answerLable = 	(TextView) rootView.findViewById(R.id.answerLable);

				enterEr.setText("0.00000001");
				
				answerLable.setText("");
				
				// ������� ������ "������"
				bHelp.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent help = new Intent(getActivity(), IntegralHelp.class);
						startActivity(help);
					}
				});
				
				// ������� ������ "���������"
				bCalculate.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						String strFormula = enterFormula.getText().toString();
						Formula formula = new Formula(strFormula);
						boolean res = formula.parse();

						if (res) {
							String strA = enterA.getText().toString();
							String strB = enterB.getText().toString();
							String strEr = enterEr.getText().toString();

							if (strA.length() == 0
									|| (strA.length() == 1 && (strA.charAt(0) == '.' || strA
											.charAt(0) == '-'))
									|| (strA.length() == 2 && strA.charAt(0) == '-' && strA
											.charAt(1) == '.')
									|| strB.length() == 0
									|| (strB.length() == 1 && (strB.charAt(0) == '.' || strB
											.charAt(0) == '-'))
									|| (strB.length() == 2 && strB.charAt(0) == '-' && strB
											.charAt(1) == '.')) {
								
								Toast.makeText(getActivity(),
										"������� ������ �������!", Toast.LENGTH_SHORT)
										.show();
								
								answerLable.setText("");
								answer.setText("");
								
								return;
							}
							
							answerLable.setText("�����: ");

							double er;
							if (strEr.length() == 0
									|| (strEr.length() == 1 && (strEr.charAt(0) == '.' || strEr
											.charAt(0) == '-'))
									|| (strEr.length() == 2 && strEr.charAt(0) == '-' && strEr
											.charAt(1) == '.'))
								er = 0.00000001;
							else
								er = Double.valueOf(strEr);

							double a = Double.valueOf(strA);
							double b = Double.valueOf(strB);
							double integral = formula.integral(a, b, 2, er);
							// System.out.println(a + " " + b);

							if (formula.integralSucceeded)
								answer.setText(String.valueOf(integral));
							else
							{
								Toast.makeText(getActivity(),
										"���������� ��������� ��������!",
										Toast.LENGTH_SHORT).show();
								
								answerLable.setText("");
								answer.setText("");
							}
						} 
						else
						{
							Toast.makeText(getActivity(), "������� ������ �������!",
									Toast.LENGTH_SHORT).show();
							
							answerLable.setText("");
							answer.setText("");
						}
					}
				});	
			} 
			else if (currentNumebr == 1) // ������� ����
			{
				rootView = inflater.inflate(R.layout.main_fragment, container,
						false);

				Button formul = (Button) rootView.findViewById(R.id.formul);	 // ������ "�������"
				Button integral = (Button) rootView.findViewById(R.id.integral); // ������ "���������"
				Button site = (Button) rootView.findViewById(R.id.site);		 // ������ "����"

				// ������ ��� �������� �� activity �������
				formul.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						ViewPager viewPager = (ViewPager) getActivity()
								.findViewById(R.id.pager);
						viewPager.setCurrentItem(2);
					}
				});

				// ������ ��� �������� �� activity ��������
				integral.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						ViewPager viewPager = (ViewPager) getActivity()
								.findViewById(R.id.pager);
						viewPager.setCurrentItem(0);
					}
				});

				// ������ ��� �������� �� ����
				site.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(
								Intent.ACTION_VIEW,
								Uri.parse("http://studentassistant.herokuapp.com/"));
						startActivity(intent);
					}
				});
				
			} 
			else // ������ ���������� ������
			{
				rootView = inflater.inflate(R.layout.formul, container, false);

				// ��������� ��������� ����� �� ������� � ���������� �����
				groupData = new ArrayList<Map<String, String>>();
				for (String group : groups) {
					// ��������� ������ ���������� ��� ������ ������
					m = new HashMap<String, String>();
					m.put("groupName", group); // ��� ��������
					groupData.add(m);
				}

				// ������ ���������� ����� ��� ������
				String groupFrom[] = new String[] { "groupName" };
				// ������ ID view-���������, � ������� ����� �������� ���������
				// �����
				int groupTo[] = new int[] { android.R.id.text1 };

				// ������� ��������� ��� ��������� ���������
				childData = new ArrayList<ArrayList<Map<String, String>>>();

				// ������� ��������� ��������� ��� ������ ������
				childDataItem = new ArrayList<Map<String, String>>();
				// ��������� ������ ���������� ��� ������� ��������
				for (String formula : mehanika) {
					m = new HashMap<String, String>();
					m.put("formula", formula); // �������� ��������
					childDataItem.add(m);
				}
				// ��������� � ��������� ���������
				childData.add(childDataItem);

				// ������� ��������� ��������� ��� ������ ������
				childDataItem = new ArrayList<Map<String, String>>();
				for (String formula : molekula) {
					m = new HashMap<String, String>();
					m.put("formula", formula);
					childDataItem.add(m);
				}
				childData.add(childDataItem);

				// ������� ��������� ��������� ��� ������� ������
				childDataItem = new ArrayList<Map<String, String>>();
				for (String formula : volni) {
					m = new HashMap<String, String>();
					m.put("formula", formula);
					childDataItem.add(m);
				}
				childData.add(childDataItem);

				// ������� ��������� ��������� ��� ������� ������
				childDataItem = new ArrayList<Map<String, String>>();
				for (String formula : optika) {
					m = new HashMap<String, String>();
					m.put("formula", formula);
					childDataItem.add(m);
				}
				childData.add(childDataItem);

				// ������� ��������� ��������� ��� ������� ������
				childDataItem = new ArrayList<Map<String, String>>();
				for (String formula : elektrichestvo) {
					m = new HashMap<String, String>();
					m.put("formula", formula);
					childDataItem.add(m);
				}
				childData.add(childDataItem);

				// ������� ��������� ��������� ��� ������� ������
				childDataItem = new ArrayList<Map<String, String>>();
				for (String formula : atom) {
					m = new HashMap<String, String>();
					m.put("formula", formula);
					childDataItem.add(m);
				}
				childData.add(childDataItem);

				// ������� ��������� ��������� ��� ������� ������
				childDataItem = new ArrayList<Map<String, String>>();
				for (String formula : yadro) {
					m = new HashMap<String, String>();
					m.put("formula", formula);
					childDataItem.add(m);
				}
				childData.add(childDataItem);

				// ������ ���������� ��������� ��� ������
				String childFrom[] = new String[] { "formula" };
				// ������ ID view-���������, � ������� ����� �������� ���������
				// ���������
				int childTo[] = new int[] { android.R.id.text1 };

				SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(
						getActivity(), groupData,
						android.R.layout.simple_expandable_list_item_1,
						groupFrom, groupTo, childData,
						android.R.layout.simple_list_item_1, childFrom, childTo);

				elvMain = (ExpandableListView) rootView
						.findViewById(R.id.elvMain);
				elvMain.setAdapter(adapter);
			}

			return rootView;
		}
	}

}
