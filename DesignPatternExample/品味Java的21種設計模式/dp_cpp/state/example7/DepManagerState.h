#pragma once

#include "LeaveRequestState.h"
#include "StateMachine.h"
#include "LeaveRequestModel.h"
#include "AuditOverState.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace state
			{
				namespace example7
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