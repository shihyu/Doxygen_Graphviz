#pragma once

#include "AbstractFactory.h"
#include "AMDCPU.h"
#include "MSIMainboard.h"

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
					/// <summary> * װ����������AMD��CPU + ΢�ǵ�����
					/// * ���ﴴ��CPU����������ʱ���Ƕ�Ӧ�ģ���ƥ���ϵ� </summary>
					/// 
					class Schema2 : public AbstractFactory
					{
					public:
						virtual object *createProduct(int type);
					};
				}
			}
		}
	}
}