package ru.andrewkir.hse_mooc.network.responses

import ru.andrewkir.hse_mooc.network.responses.CoursesPreview.CoursePreview

data class ViewedCoursesResponse (
    val viewedCourses: List<CoursePreview>
)