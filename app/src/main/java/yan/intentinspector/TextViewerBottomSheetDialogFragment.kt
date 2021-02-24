package yan.intentinspector

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import yan.intentinspector.databinding.TextViewerFragmentBinding

class TextViewerBottomSheetDialogFragment: BottomSheetDialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.text_viewer_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = TextViewerFragmentBinding.bind(view)

        val textUri: Uri = arguments!!.getParcelable("textUri")!!

        val textContent = requireContext().contentResolver
                .openInputStream(textUri)!!.use {
                    it.reader().readText()
                }

        binding.textView.text = textContent
    }
}