#pragma once

#include "Api.h"
#include "Impl.h"
#include "Impl2.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace simplefactory
			{
				namespace example4
				{

					///
					/// <summary> * �����࣬��������Api�� </summary>
					/// 
					class Factory
					{
					///	
					///	 <summary> * ����Ĵ���Api�ķ��������ݿͻ��˵Ĳ����������ӿ� </summary>
					///	 * <param name="type"> �ͻ��˴����ѡ����ӿڵ����� </param>
					///	 * <returns> ����õ�Api���� </returns>
					///	 
					public:
						static Api *createApi(int type);
					};
				}
			}
		}
	}
}