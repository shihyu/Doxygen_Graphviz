#pragma once

#include "Colleague.h"
#include "Mediator.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace mediator
			{
				namespace example1
				{
					///
					/// <summary> * �����ͬ����A </summary>
					/// 
					class ConcreteColleagueA : public Colleague
					{
					public:
						ConcreteColleagueA(Mediator *mediator);
					///	
					///	 <summary> * ʾ�ⷽ����ִ��ĳЩҵ���� </summary>
					///	 
						virtual void someOperation();
					};
				}
			}
		}
	}
}