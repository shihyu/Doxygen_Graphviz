#pragma once

#include "LeaveRequestState.h"
#include "StateMachine.h"
#include "LeaveRequestModel.h"

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
					/// <summary> * ������˽������� </summary>
					/// 
					class AuditOverState : public LeaveRequestState
					{
					public:
						virtual void doWork(StateMachine *request);
					};

				}
			}
		}
	}
}