package ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.biddlr.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditPaymentMethodFragment extends Fragment {
    public EditPaymentMethodFragment() {
        // Required empty public constructor
    }

    public static EditPaymentMethodFragment newInstance() {
        EditPaymentMethodFragment epmf = new EditPaymentMethodFragment();
        return epmf;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_payment_method, container, false);
    }
}
