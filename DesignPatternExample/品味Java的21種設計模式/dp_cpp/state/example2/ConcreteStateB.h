#pragma once

#include "State.h"
#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace state
			{
				namespace example2
				{


					///
					/// <summary> * ʵ��һ����Context��һ���ض�״̬��ص���Ϊ </summary>
					/// 
					class ConcreteStateB : public State
					{
					public:
						virtual void handle(std::string sampleParameter);
					};
				}
			}
		}
	}
}