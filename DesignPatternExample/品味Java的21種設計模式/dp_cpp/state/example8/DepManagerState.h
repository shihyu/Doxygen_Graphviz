#pragma once

#include "LeaveRequestState.h"
#include "StateMachine.h"
#include "LeaveRequestModel.h"
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
					/// <summary> * �����ž������ˣ�������Ӧ��˽���״̬ </summary>
					/// 
					class DepManagerState : public LeaveRequestState
					{
					public:
						virtual void doWork(StateMachine *request);
					};

				}
			}
		}
	}
}