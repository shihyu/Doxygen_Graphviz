#pragma once

#include "AbstractFactory.h"
#include "IntelCPU.h"
#include "GAMainboard.h"
#include "HyMemory.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace abstractfactory
			{
				namespace example4
				{

					///
					/// <summary> * װ����������Intel ��CPU + ���ε����� + �ִ����ڴ� </summary>
					/// 
					class Schema3 : public AbstractFactory
					{
					public:
						virtual object *createProduct(int type);
					};

				}
			}
		}
	}
}