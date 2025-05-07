package org.example.data.datasource.changelog

import org.example.logic.entity.ChangeHistory

interface ChangeHistoryDataSource {
    fun addChangeHistory(changeHistory: ChangeHistory):ChangeHistory
    fun getByProjectId(projectId:Int):List<ChangeHistory>
    fun getByTaskId(taskId:Int):List<ChangeHistory>
}