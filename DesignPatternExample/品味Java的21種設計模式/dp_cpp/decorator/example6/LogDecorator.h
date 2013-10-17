#pragma once

#include "Decorator.h"
#include "GoodsSaleEbi.h"
#include "SaleModel.h"
#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace decorator
			{
				namespace example6
				{
					///
					/// <summary> * ʵ����־��¼ </summary>
					/// 
					class LogDecorator : public Decorator
					{
					public:
						LogDecorator(GoodsSaleEbi *ebi);

						virtual bool sale(std::string user, std::string customer, SaleModel *saleModel);

					};

				}
			}
		}
	}
}