package com.example.todoapp.view.ui.fragment.add

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentAddBinding
import com.example.todoapp.model.entities.TodoData
import com.example.todoapp.util.listener
import com.example.todoapp.util.parsePriority
import com.example.todoapp.util.verifyDataCheck
import com.example.todoapp.viewmodel.TodoViewModel
import kotlinx.coroutines.launch

class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private lateinit var todoViewModel: TodoViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        binding.spinnerPriorities.onItemSelectedListener = context?.let { listener(it) }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        todoViewModel = ViewModelProvider(this).get(TodoViewModel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_check) {
            insertDataToDb()
        }
        return super.onOptionsItemSelected(item)
    }


    private fun insertDataToDb() {
        val mTitle = binding.titleEdit.text.toString()
        val mPriority = binding.spinnerPriorities.selectedItem.toString()
        val mDescription = binding.descriptionEdit.text.toString()


        val validation = verifyDataCheck(mTitle, mDescription)
        if (validation) {
            val data = TodoData(0, mTitle, parsePriority(mPriority), mDescription)

            lifecycleScope.launch {
                todoViewModel.insert(data)
            }
            Toast.makeText(context, "Successfully added", Toast.LENGTH_SHORT).show()
            findNavController().navigate(AddFragmentDirections.actionAddFragmentToListFragment())
        } else {
            Toast.makeText(context, "Please fill out all fields", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}