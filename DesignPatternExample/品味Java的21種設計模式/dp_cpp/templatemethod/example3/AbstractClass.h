#pragma once

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace templatemethod
			{
				namespace example3
				{
					///
					/// <summary> * ����ģ�巽����ԭ������ȵĳ����� </summary>
					/// 
					class AbstractClass
					{
					///	
					///	 <summary> * ԭ�����1����νԭ��������ǳ���Ĳ���������Ҫ�������ṩʵ�� </summary>
					///	 
					public:
						virtual void doPrimitiveOperation1() = 0;
					///	
					///	 <summary> * ԭ�����2 </summary>
					///	 
						virtual void doPrimitiveOperation2() = 0;
					///	
					///	 <summary> * ģ�巽���������㷨�Ǽ� </summary>
					///	 
						void templateMethod();
					};


				}
			}
		}
	}
}