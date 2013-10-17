#pragma once

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace templatemethod
			{
				namespace example5
				{
					///
					/// <summary> * һ����Ϊ������ģ�涨��ʾ�� </summary>
					/// 
					class AbstractTemplate
					{
					///	
					///	 <summary> * ģ�巽���������㷨�Ǽ� </summary>
					///	 
					public:
						void templateMethod();
					///	
					///	 <summary> * �������1���㷨�еĲ��裬�̶�ʵ�֣��������಻��Ҫ���� </summary>
					///	 
					private:
						void operation1();
					///	
					///	 <summary> * �������2���㷨�еĲ��裬�̶�ʵ�֣����������Ҫ���ʣ�
					///	 * ��ȻҲ���Զ����public�ģ������Ա����ǣ������final�� </summary>
					///	 
					protected:
						void operation2();
					///	
					///	 <summary> * �����AbstractClass����������Ĺ������ܣ�
					///	 * ��ͨ�����Ǿ�����㷨���� </summary>
					///	 
						virtual void commonOperation();
					///	
					///	 <summary> * ԭ�����1���㷨�еı�Ҫ���裬�����޷�ȷ���������ʵ�֣���Ҫ������ʵ�� </summary>
					///	 
						virtual void doPrimitiveOperation1() = 0;
					///	
					///	 <summary> * ԭ�����2���㷨�еı�Ҫ���裬�����޷�ȷ���������ʵ�֣���Ҫ������ʵ�� </summary>
					///	 
						virtual void doPrimitiveOperation2() = 0;
					///	
					///	 <summary> * ���Ӳ������㷨�еĲ��裬��һ����Ҫ���ṩȱʡʵ��
					///	 * ������ѡ�񲢾���ʵ�� </summary>
					///	 
						virtual void hookOperation1();
					///	
					///	 <summary> * ��������������ĳ������������Object�����ˣ����㷨ʵ���п�����Ҫ </summary>
					///	 * <returns> ������ĳ���㷨ʵ����Ҫ�Ķ��� </returns>
					///	 
						virtual object *createOneObject() = 0;
					};

				}
			}
		}
	}
}