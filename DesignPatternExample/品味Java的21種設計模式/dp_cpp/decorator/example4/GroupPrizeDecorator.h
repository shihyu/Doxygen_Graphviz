#pragma once

#include "Decorator.h"
#include "Component.h"
#include "TempDB.h"
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
					/// <summary> * װ�������󣬼��㵱���Ŷ�ҵ�񽱽� </summary>
					/// 
					class GroupPrizeDecorator : public Decorator
					{
					public:
						GroupPrizeDecorator(Component *c);

						virtual double calcPrize(std::string user, Date *begin, Date *end);

					};

				}
			}
		}
	}
}