package com.example.studentprofile

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.example.studentprofile.databinding.ActivityMainBinding
import com.example.studentprofile.model.Student
import com.example.studentprofile.utils.gone
import com.example.studentprofile.utils.show
import com.example.studentprofile.utils.toAcademicRanking
import com.example.studentprofile.utils.toast
import com.example.studentprofile.utils.trimmedText
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val defaultStudent = Student(
        id = "22505120005",
        name = "Nguyễn Văn An",
        className = "22CT111",
        email = "an.nv@ute.udn.vn",
        gpa = 3.75
    )
    private var currentStudent = defaultStudent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bindStudentData(currentStudent)
        setupListeners()
    }

    private fun setupListeners() = with(binding) {
        btnUpdateGpa.setOnClickListener {
            updateGpa()
        }

        edtGpaInput.doOnTextChanged { text, _, _, _ ->
            edtGpaInput.error = null

            text?.toString()
                ?.trim()
                ?.takeIf(String::isNotEmpty)
                ?.toDoubleOrNull()
                ?.takeIf { it in GPA_MIN..GPA_MAX }
                ?.let { previewGpa ->
                    tvPreviewRanking.text = getString(
                        R.string.ranking_preview,
                        previewGpa.toAcademicRanking()
                    )
                    tvPreviewRanking.show()
                } ?: tvPreviewRanking.gone()
        }
    }

    private fun updateGpa() {
        val rawInput = binding.edtGpaInput.trimmedText()
        val newGpa = rawInput.toDoubleOrNull()

        if (newGpa == null || newGpa !in GPA_MIN..GPA_MAX) {
            binding.edtGpaInput.error = getString(R.string.gpa_validation_error)
            binding.edtGpaInput.requestFocus()
            toast(getString(R.string.invalid_gpa_message))
            return
        }

        newGpa.let { validGpa ->
            currentStudent = currentStudent.copy(gpa = validGpa).also { updatedStudent ->
                Log.d(TAG, "Updated GPA to ${updatedStudent.gpa}")
            }
        }

        bindStudentData(currentStudent)
        binding.edtGpaInput.apply {
            error = null
            text.clear()
        }
        toast(getString(R.string.update_success))
    }

    private fun bindStudentData(student: Student) = with(binding) {
        student.run {
            tvStudentName.text = name
            tvStudentDetails.text = getString(R.string.student_details_format, id, className)
            tvStudentEmail.text = email
            tvGpaBadge.text = String.format(
                Locale.US,
                getString(R.string.gpa_badge_format),
                gpa,
                gpa.toAcademicRanking()
            )
        }
    }

    private companion object {
        const val TAG = "StudentProfile"
        const val GPA_MIN = 0.0
        const val GPA_MAX = 4.0
    }
}
