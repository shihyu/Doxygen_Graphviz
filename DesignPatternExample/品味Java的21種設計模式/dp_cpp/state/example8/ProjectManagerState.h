#pragma once

#include "LeaveRequestState.h"
#include "StateMachine.h"
#include "LeaveRequestModel.h"
#include "DepManagerState.h"
#include "AuditOverState.h"
#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace state
			{
				namespace example8
				{

					///
					/// <summary> * ������Ŀ�������ˣ��������ܶ�Ӧ���ž�����ˡ���˽���֮�е�һ�� </summary>
					/// 
					class ProjectManagerState : public LeaveRequestState
					{
					public:
						virtual void doWork(StateMachine *request);
					};

				}
			}
		}
	}
}