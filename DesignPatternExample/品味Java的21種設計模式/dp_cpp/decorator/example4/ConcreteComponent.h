#pragma once

#include "Component.h"
#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace decorator
			{
				namespace example4
				{


					///
					/// <summary> * ������ʵ�ּ��㽱����࣬Ҳ�Ǳ�װ����װ�εĶ��� </summary>
					/// 
					class ConcreteComponent : public Component
					{

					public:
						virtual double calcPrize(std::string user, Date *begin, Date *end);
					};

				}
			}
		}
	}
}