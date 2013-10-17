#pragma once

#include "State.h"

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
					/// <summary> * ����״̬��������൱��״̬ģʽ��Context
					/// * ������������ʹ��״̬ģʽʱ�Ĺ������� </summary>
					/// 
					class StateMachine
					{
					///	
					///	 <summary> * ����һ��״̬���� </summary>
					///	 
					private:
						State *state;
					///	
					///	 <summary> * �������̴�����Ҫ��ҵ�����ݶ��󣬲�֪����������,
					///	 * ��Object������ֻ�Ǵ��ݵ������״̬�������� </summary>
					///	 
						object *businessVO;
					///	
					///	 <summary> * ִ�й������ͻ��˴������̵Ľӿڷ�����
					///	 * �ڿͻ�����Լ���ҵ��������� </summary>
					///	 
					public:
						virtual void doWork();

						virtual State *getState();
						virtual void setState(State *state);
						virtual object *getBusinessVO();
						virtual void setBusinessVO(object *businessVO);
					};

				}
			}
		}
	}
}