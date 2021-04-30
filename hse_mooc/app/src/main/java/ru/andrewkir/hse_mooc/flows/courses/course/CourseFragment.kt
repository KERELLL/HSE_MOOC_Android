package ru.andrewkir.hse_mooc.flows.courses.course

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import ru.andrewkir.hse_mooc.R
import ru.andrewkir.hse_mooc.common.BaseFragment
import ru.andrewkir.hse_mooc.common.openLink
import ru.andrewkir.hse_mooc.databinding.FragmentCoursePageBinding
import ru.andrewkir.hse_mooc.flows.courses.search.CoursesSearchRepository
import ru.andrewkir.hse_mooc.flows.courses.search.CoursesSearchViewModel
import ru.andrewkir.hse_mooc.network.api.CoursesApi
import ru.andrewkir.hse_mooc.network.responses.CoursesSearch.Course
import java.text.DecimalFormat
import java.util.*


class CourseFragment :
    BaseFragment<CoursesSearchViewModel, CoursesSearchRepository, FragmentCoursePageBinding>() {

    companion object {
        const val COURSE_TAG = "courseItem"

        fun newInstance(course: Course): CourseFragment {
            val args = Bundle()
            args.putParcelable(COURSE_TAG, course)
            val fragment = CourseFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun provideViewModelClass() = CoursesSearchViewModel::class.java

    override fun provideRepository(): CoursesSearchRepository {
        return CoursesSearchRepository(
            apiProvider.provideApi(
                CoursesApi::class.java,
                requireContext(),
                userPrefsManager.obtainAccessToken()
            )
        )
    }

    override fun provideBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCoursePageBinding = FragmentCoursePageBinding.inflate(inflater, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val course = arguments?.getParcelable<Course>(COURSE_TAG)

        course?.let {
            bind.courseName.text = it.courseName

            Glide.with(requireContext())
                .load(it.previewImageLink)
                .placeholder(ru.andrewkir.hse_mooc.R.drawable.course_image_overlay)
                .into(bind.courseImage)

            bind.button.setOnClickListener {
                openLink(course.link)
            }

            if (it.price.amount != 0.0) {
                val currency = Currency.getInstance(it.price.currency)
                val symbol: String = currency.getSymbol(Locale("ru", "RU"))
                val format = DecimalFormat("0.#")
                bind.courseCost.text = format.format(it.price.amount) + " $symbol"
            } else {
                bind.courseCost.text = "Бесплатно"
            }

            bind.courseLanguage.text = it.courseLanguages.joinToString(", ").toUpperCase()

            bind.courseVendor.text = it.vendor
            //TODO bind.courseVendorIcon
            Glide.with(requireContext())
                .load(it.author.icon)
                .placeholder(ru.andrewkir.hse_mooc.R.drawable.designer)
                .into(bind.courseVendorIcon)

            bind.courseAuthor.text = it.author.name
            Glide.with(requireContext())
                .load(it.author.icon)
                .placeholder(ru.andrewkir.hse_mooc.R.drawable.designer)
                .into(bind.courseAuthorIcon)
            bind.courseAuthor.setOnClickListener { _ ->
                openLink(it.author.link)
            }

            bind.ratingVendorText.text = "Рейтинг ${it.vendor}"
            bind.ratingVendor.rating = it.rating.external.averageScore.toFloat()

            bind.ratingInternal.rating = it.rating.internal.averageScore.toFloat()

            bind.courseDescription.text = it.description

            for (category in listOf("1", "2", "3", "4", "5")) {
                val chip =
                    this.layoutInflater.inflate(R.layout.chip_category_page, bind.categoriesChipGroup, false) as Chip
                chip.id = View.generateViewId()
                chip.text = "$category"
                chip.setOnClickListener {

                }
                bind.categoriesChipGroup.addView(chip)
            }
        }
    }
}