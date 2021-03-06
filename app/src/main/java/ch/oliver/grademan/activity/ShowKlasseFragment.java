package ch.oliver.grademan.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import ch.oliver.grademan.R;
import ch.oliver.grademan.adapter.FachArrayAdapter;
import ch.oliver.grademan.database.FachDAO;
import ch.oliver.grademan.database.KlasseDAO;
import ch.oliver.grademan.model.Fach;
import ch.oliver.grademan.model.Klasse;


public class ShowKlasseFragment extends Fragment {

    MainActivity mainActivity;
    View myView;
    TextView textView;
    ListView listView;
    private KlasseDAO kdao;
    private Klasse currentKlasse;

    public ShowKlasseFragment() {
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mainActivity = (MainActivity) getActivity();
        this.myView = inflater.inflate(R.layout.klassen_fragment, container, false);
        kdao = new KlasseDAO(this.getActivity());
        textView = (TextView) myView.findViewById(R.id.classname);
        listView = (ListView) myView.findViewById(R.id.classliste);
        Bundle args = getArguments();
        FachDAO fdao = new FachDAO(getContext());
        currentKlasse = kdao.getKlasseById(args.getInt("class_id", 0));
        getActivity().setTitle(currentKlasse.getKlassenname());
        List<Fach> facher;
        facher = fdao.getAllFacherfromClass(currentKlasse);
        currentKlasse.setFaecher(facher);
        mainActivity.setCurrenKlasse(currentKlasse);
        textView.setText(args.getString("classname"));
        final FachArrayAdapter fachArrayAdapter = new FachArrayAdapter(getContext(), getActivity().getLayoutInflater(), facher);
        listView.setAdapter(fachArrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment classFragment = new ShowNotenFragment();
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                Fach fach = fachArrayAdapter.getItem(position);
                if(fach !=null) {
                    Bundle args = new Bundle();
                    args.putInt("fach_id", fach.getId_fach());
                    args.putString("fachname", fach.getName());
                    classFragment.setArguments(args);
                }
                fragmentManager.beginTransaction().replace(R.id.flContent, classFragment, "noten_fragment").addToBackStack(null).commit();
            }

        });
        setHasOptionsMenu(true);
        return myView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        if (currentKlasse.getIs_favorite_klasse() == 1) {
            inflater.inflate(R.menu.menu_favorite_klasse, menu);
        } else {
            inflater.inflate(R.menu.menu_klasse, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
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
        } else if (id == R.id.addfach) {
            NewFachDialogFragment dialogfragment = new NewFachDialogFragment();
            dialogfragment.show(getFragmentManager(), "");
        } else if (id == R.id.favoriteKlasse || id==R.id.isFavoriteKlasse) {
            Klasse klasse = kdao.getKlasseById(currentKlasse.getId_klasse());
            if (klasse.getIs_favorite_klasse()==1){//item.getIcon() == getResources().getDrawable(R.drawable.ic_is_favorite)) {
                item.setIcon(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_make_favorite,null));
                klasse.setIs_favorite_klasse(0);
            } else {
                item.setIcon( ResourcesCompat.getDrawable(getResources(),R.drawable.ic_is_favorite,null));
                klasse.setIs_favorite_klasse(1);
            }
            kdao.makeFavoriteKlasse(klasse);
        }

        return super.onOptionsItemSelected(item);
    }

}
