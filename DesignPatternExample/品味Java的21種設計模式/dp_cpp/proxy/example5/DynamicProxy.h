#pragma once

#include "OrderApi.h"
#include "Order.h"
#include "Proxy.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace proxy
			{
				namespace example5
				{

					///
					/// <summary> * ʹ��Java�еĶ�̬���� </summary>
					/// 
					class DynamicProxy : public InvocationHandler
					{
					///	
					///	 <summary> * ������Ķ��� </summary>
					///	 
					private:
						OrderApi *order;
					///	
					///	 <summary> * ��ȡ�󶨺ô���;���Ŀ�������Ŀ�����Ľӿ� </summary>
					///	 * <param name="order"> ����Ķ��������൱�ھ���Ŀ����� </param>
					///	 * <returns> �󶨺ô���;���Ŀ�������Ŀ�����Ľӿ� </returns>
					///	 
					public:
						virtual OrderApi *getProxyInterface(Order *order);

						virtual object *invoke(object *proxy, Method *method, object args[]) throw(Throwable);
					};

				}
			}
		}
	}
}