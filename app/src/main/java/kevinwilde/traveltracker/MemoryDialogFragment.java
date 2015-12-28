package kevinwilde.traveltracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Kevin Wilde on 12/22/2015.
 */
public class MemoryDialogFragment extends DialogFragment {

    private static final String MEMORY_KEY = "MEMORY";

    private Memory mMemory;
    private Listener mListener;
    private View mView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mMemory = (Memory) args.getSerializable(MEMORY_KEY);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mView = getActivity().getLayoutInflater().inflate(R.layout.memory_dialog_fragment, null);
        TextView city = (TextView) mView.findViewById(R.id.city);
        TextView country = (TextView) mView.findViewById(R.id.country);
        city.setText(mMemory.city);
        country.setText(mMemory.country);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(mView)
                .setTitle(R.string.memory_dialog_title)
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText notes = (EditText) mView.findViewById(R.id.notes);
                        mMemory.notes = notes.getText().toString();
                        mListener.OnSaveClicked(mMemory);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.OnCancelClicked(mMemory);
            }
        });
        return builder.create();
    }

    public static MemoryDialogFragment newInstance(Memory memory) {
        Bundle args = new Bundle();
        args.putSerializable(MEMORY_KEY, memory);
        MemoryDialogFragment fragment = new MemoryDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public interface Listener {
        public void OnSaveClicked(Memory memory);
        public void OnCancelClicked(Memory memory);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (Listener) activity;
        }
        catch (ClassCastException e){
            throw new IllegalStateException("Activity does not implement MemoryDialogFragment.Listener");
        }
    }
}
