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
					/// <summary> * �����ͬ����B </summary>
					/// 
					class ConcreteColleagueB : public Colleague
					{
					public:
						ConcreteColleagueB(Mediator *mediator);
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