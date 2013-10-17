#pragma once

#include "Decorator.h"
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
					/// <summary> * װ�������󣬼����ۼƽ��� </summary>
					/// 
					class SumPrizeDecorator : public Decorator
					{
					public:
						SumPrizeDecorator(Component *c);

						virtual double calcPrize(std::string user, Date *begin, Date *end);

					};

				}
			}
		}
	}
}