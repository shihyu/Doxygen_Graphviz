#pragma once

#include "Api.h"
#include "ImplA.h"
#include "ImplB.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace simplefactory
			{
				namespace example2
				{
					///
					/// <summary> * �����࣬��������Api���� </summary>
					/// 
					class Factory
					{
					///	
					///	 <summary> * ����Ĵ���Api����ķ��� </summary>
					///	 * <param name="condition"> ʾ�⣬���ⲿ�����ѡ������ </param>
					///	 * <returns> ����õ�Api���� </returns>
					///	 
					public:
						static Api *createApi(int condition);
					};
				}
			}
		}
	}
}