#pragma once

#include "StateMachine.h"

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
					/// <summary> * ����״̬�ӿ� </summary>
					/// 
					class State
					{
					///	
					///	 <summary> * ִ��״̬��Ӧ�Ĺ��ܴ��� </summary>
					///	 * <param name="ctx"> �����ĵ�ʵ������ </param>
					///	 
					public:
						virtual public void doWork(StateMachine*) = 0;
					};

				}
			}
		}
	}
}