package org.example.logic.repository

import org.example.logic.entity.Project

interface ChangeHistoryRepository {
        fun ShowHistoryByProjectID(projectId: Int): Result<Project>
}