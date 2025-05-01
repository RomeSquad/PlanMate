package org.example.presentation.formatter.dataFormatter

import org.example.logic.entity.Project


fun Project.format(): String {
    return """
            Project Name: $name
            Project Description: $description
        """.trimIndent()
}


fun Project.swimlaneFormat(): String {
    TODO()
}

